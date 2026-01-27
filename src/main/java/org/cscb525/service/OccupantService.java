package org.cscb525.service;

import jakarta.validation.Valid;
import org.cscb525.dao.OccupantDao;
import org.cscb525.dto.occupant.OccupantDto;
import org.cscb525.dto.occupant.UpdateOccupantDto;
import org.cscb525.entity.Occupant;
import org.cscb525.exceptions.EmptyListException;

import java.util.List;

public class OccupantService {
    public void updateOccupant(@Valid UpdateOccupantDto occupantDto) {
        OccupantDao.updateOccupant(occupantDto);
    }

    public OccupantDto getOccupantById(long id) {
        return OccupantDao.findOccupantDtoById(id);
    }

    public List<OccupantDto> getAllOccupants() {
        List<OccupantDto> occupants = OccupantDao.findAllOccupants();
        if (occupants.isEmpty()) throw new EmptyListException(Occupant.class);
        return occupants;
    }

    public List<OccupantDto> getAllOccupantsInBuildingOrderedAlphabetically(long buildingId) {
        List<OccupantDto> occupants = OccupantDao.occupantsFindByBuildingOrderByNameAsc(buildingId);
        if (occupants.isEmpty()) throw new EmptyListException(Occupant.class);
        return occupants;
    }

    public List<OccupantDto> getAllOccupantsInBuildingOrderedByAge(long buildingId) {
        List<OccupantDto> occupants = OccupantDao.occupantsFindByBuildingOrderByAgeDesc(buildingId);
        if (occupants.isEmpty()) throw new EmptyListException(Occupant.class);
        return occupants;
    }

    public int getOccupantCountInBuilding(long buildingId) {
        return (int) OccupantDao.findOccupantCountByBuilding(buildingId);
    }
}
