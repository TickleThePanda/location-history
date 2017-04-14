package co.uk.ticklethepanda.location.history.cartograph;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by panda on 4/13/17.
 */
public interface PointConverter<I extends Point, O extends Point> {

    O convert(I input);

    default List<O> convertList(List<I> input) {
        return input.stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }
}
