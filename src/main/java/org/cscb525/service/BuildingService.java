package org.cscb525.service;

import org.cscb525.dao.BuildingDao;
import org.cscb525.dto.building.BuildingDto;
import org.cscb525.dto.building.UpdateBuildingDto;
import org.cscb525.entity.Building;
import org.cscb525.exceptions.EmptyListException;

import java.util.List;

public class BuildingService {
    public BuildingDto updateBuilding(UpdateBuildingDto buildingDto) {
        BuildingDao.updateBuilding(buildingDto);
        return BuildingDao.findBuildingDtoById(buildingDto.getId());
    }

    public BuildingDto getBuildingById(long id) {
        return BuildingDao.findBuildingDtoById(id);
    }

    public List<BuildingDto> getAllBuildings() {
        List<BuildingDto> buildings = BuildingDao.findAllBuildings();
        if (buildings.isEmpty()) throw new EmptyListException(Building.class);
        return buildings;
    }

    public int getBuildingCountOfCompany(long companyId) {
        return (int) BuildingDao.findAllBuildingCountByCompany(companyId);
    }

    public List<BuildingDto> getAllBuildingsByEmployee(long employeeId) {
        List<BuildingDto> buildings = BuildingDao.findAllBuildingsByEmployee(employeeId);
        if (buildings.isEmpty()) throw new EmptyListException(Building.class);
        return buildings;
    }

    public List<BuildingDto> getAllBuildingsByCompany(long companyId) {
        List<BuildingDto> buildings = BuildingDao.findAllBuildingsByCompany(companyId);
        if (buildings.isEmpty()) throw new EmptyListException(Building.class);
        return buildings;
    }
}
