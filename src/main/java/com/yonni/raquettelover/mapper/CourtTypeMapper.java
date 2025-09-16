package com.yonni.raquettelover.mapper;

import com.yonni.raquettelover.dto.CourtTypeOutDto;
import com.yonni.raquettelover.enumeration.CourtType;

public class CourtTypeMapper {
    private CourtTypeMapper() {
    }

    public static CourtTypeOutDto toDto(CourtType courtType) {
        return new CourtTypeOutDto(  
                courtType.name(), // le nom de l’enum              
                courtType.getLabel(),                
                courtType.getMinPlayers()
        );
    }
}
