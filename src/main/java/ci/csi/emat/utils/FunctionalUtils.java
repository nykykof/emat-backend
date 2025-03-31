package ci.csi.emat.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.function.Supplier;


@Slf4j
public final class FunctionalUtils {

    private FunctionalUtils() {
        throw new UnsupportedOperationException("FunctionalUtils may not be instantiated");
    }

    public static <T> Optional<T> getOrEmpty(Supplier<T> supplier) {
        try {
            return Optional.ofNullable(supplier.get());
        } catch (NullPointerException | ArrayIndexOutOfBoundsException e) {
            log.warn("Error while getting data {}", e.getMessage());
            return Optional.empty();
        }
    }

    public static <T> T getOrNull(Supplier<T> supplier) {
        return getOrEmpty(supplier).orElse(null);
    }
}
