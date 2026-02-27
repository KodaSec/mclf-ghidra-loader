package mclfloader;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;

import ghidra.app.util.bin.BinaryReader;
import ghidra.app.util.bin.ByteProvider;
import ghidra.app.util.opinion.AbstractLibrarySupportLoader;
import ghidra.app.util.opinion.LoadSpec;
import ghidra.program.flatapi.FlatProgramAPI;
import ghidra.program.model.address.Address;
import ghidra.program.model.data.DataUtilities;
import ghidra.program.model.data.DataUtilities.ClearDataMode;
import ghidra.program.model.lang.LanguageCompilerSpecPair;
import ghidra.program.model.listing.Program;
import ghidra.program.model.mem.MemoryBlock;
import ghidra.program.model.util.CodeUnitInsertionException;
import ghidra.util.Msg;
import ghidra.util.exception.CancelledException;

public class MCLFLoader extends AbstractLibrarySupportLoader {
    MCLFHeader header;

    @Override
    public String getName() {
        return "MobiCore Loadable Format (MCLF)";
    }

    @Override
    public Collection<LoadSpec> findSupportedLoadSpecs(ByteProvider provider) throws IOException {
        BinaryReader reader = new BinaryReader(provider, true);
        if (reader.readNextAsciiString(4).equals("MCLF"))
            return List.of(new LoadSpec(this, 0, new LanguageCompilerSpecPair("ARM:LE:32:v7", "default"), true));
        return List.of();
    }

    @Override
    protected void load(Program program, ImporterSettings settings)
            throws CancelledException, IOException {
        ByteProvider provider = settings.provider();
        BinaryReader reader = new BinaryReader(provider, true);
        FlatProgramAPI api = new FlatProgramAPI(program, settings.monitor());

        header = new MCLFHeader(api, reader);

        InputStream input = provider.getInputStream(0);
        createSegment(api, input, ".text", header.textVa, header.textLen, true, false, true);
        createSegment(api, input, ".data", header.dataVa, header.dataLen, true, true, false);
        createSegment(api, null, ".bss", header.dataVa.add(header.dataLen), header.bssLen, true, true, false);

        api.addEntryPoint(header.entry);
        api.createFunction(header.entry, "_entry");

        try {
            api.createLabel(header.textVa.add(0x8c), "tlApiLibEntry", true);
        } catch (Exception e) {
            Msg.error(this, e.getMessage());
        }

        try {
            DataUtilities.createData(program, header.textVa, header.toDataType(), -1, false,
                    ClearDataMode.CLEAR_ALL_UNDEFINED_CONFLICT_DATA);
        } catch (CodeUnitInsertionException e) {
            Msg.error(this, e.getMessage());
        }
    }

    private void createSegment(FlatProgramAPI api, InputStream input, String name, Address start, long length,
            boolean read, boolean write, boolean exec) {
        try {
            MemoryBlock text = api.createMemoryBlock(name, start, input, length, false);
            text.setRead(read);
            text.setWrite(write);
            text.setExecute(exec);
        } catch (Exception e) {
            Msg.error(this, e.getMessage());
        }
    }
}
