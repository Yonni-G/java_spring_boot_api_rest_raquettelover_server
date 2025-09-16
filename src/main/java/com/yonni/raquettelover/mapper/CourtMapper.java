package com.yonni.raquettelover.mapper;

import com.yonni.raquettelover.dto.CourtOutDto;
import com.yonni.raquettelover.dto.CourtTypeOutDto;
import com.yonni.raquettelover.entity.Court;

public class CourtMapper {
    private CourtMapper() {}
    
    public static CourtOutDto toDto(Court court) {
        
        CourtTypeOutDto typeDto = CourtTypeMapper.toDto(court.getType());

        return new CourtOutDto(
                court.getId(),
                court.getName(),
                court.getDescription(),
                typeDto,
                court.getCreatedAt()
        );
    }
}
