package avg.source;

import avg.model.EntryDto;

import java.util.function.Consumer;

public interface Source extends Consumer<EntryDto> {
}
