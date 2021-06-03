package com.luizalabs.api.customer.domain.shared;

import java.util.Set;

import org.apache.commons.compress.utils.Sets;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GenericResponse<D> {
    
    private D data;
    private Set<String> messages;

    public static <D> GenericResponse<D> success(D data) {
        return GenericResponse.<D>builder().data(data).build();
    }

    public static <D> GenericResponse<D> success(D data, String message) {
        return GenericResponse.<D>builder().data(data).messages(Sets.newHashSet(message)).build();
    }

    public static <D> GenericResponse<D> success(D data, Set<String> messages) {
        return GenericResponse.<D>builder().data(data).messages(messages).build();
    }

    public static <D> GenericResponse<D> fail(Set<String> fails) {
        return GenericResponse.<D>builder().messages(fails).build();
    }

}
