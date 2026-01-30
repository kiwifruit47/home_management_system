package org.cscb525.service;

import org.cscb525.dao.OwnerDao;
import org.cscb525.dto.owner.CreateOwnerDto;
import org.cscb525.dto.owner.OwnerDto;
import org.cscb525.entity.Owner;
import org.cscb525.exceptions.EmptyListException;

import java.util.List;

public class OwnerService {
    public void createOwner(CreateOwnerDto ownerDto) {
        OwnerDao.createOwner(ownerDto);
    }

    public OwnerDto updateOwnerName(long id, String name) {
        OwnerDao.updateOwnerName(id, name);
        return OwnerDao.findOwnerDtoById(id);
    }

    public OwnerDto getOwnerById(long id) {
        return OwnerDao.findOwnerDtoById(id);
    }

    public List<OwnerDto> getAllOwners() {
        List<OwnerDto> owners = OwnerDao.findAllOwners();
        if (owners.isEmpty()) throw new EmptyListException(Owner.class);
        return owners;
    }

    public List<OwnerDto> getAllApartmentOwners(long apartmentId) {
        List<OwnerDto> owners = OwnerDao.findAllOwnersByApartment(apartmentId);
        if (owners.isEmpty()) throw new EmptyListException(Owner.class);
        return owners;
    }
}
