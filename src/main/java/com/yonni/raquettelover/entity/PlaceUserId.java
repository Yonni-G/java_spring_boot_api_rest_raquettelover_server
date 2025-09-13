package com.yonni.raquettelover.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlaceUserId implements Serializable {
    private Long userId;
    private Long placeId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PlaceUserId)) return false;
        PlaceUserId that = (PlaceUserId) o;
        return Objects.equals(getUserId(), that.getUserId()) &&
                Objects.equals(getPlaceId(), that.getPlaceId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserId(), getPlaceId());
    }
}
