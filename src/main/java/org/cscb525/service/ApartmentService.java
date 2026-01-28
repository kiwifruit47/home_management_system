package org.cscb525.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.cscb525.config.SessionFactoryUtil;
import org.cscb525.dao.ApartmentDao;
import org.cscb525.dao.OccupantDao;
import org.cscb525.dao.OwnerDao;
import org.cscb525.dto.apartment.ApartmentDto;
import org.cscb525.dto.apartment.CreateApartmentDto;
import org.cscb525.dto.occupant.CreateOccupantDto;
import org.cscb525.dto.occupant.OccupantDto;
import org.cscb525.dto.owner.OwnerDto;
import org.cscb525.entity.Apartment;
import org.cscb525.entity.Occupant;
import org.cscb525.entity.Owner;
import org.cscb525.exceptions.NotFoundException;
import org.cscb525.service.util.PetUpdateType;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class ApartmentService {
    public void createApartment(@Valid CreateApartmentDto createApartmentDto) {
        ApartmentDao.createApartment(createApartmentDto);
    }

    public ApartmentDto updateApartmentPetCount(long apartmentId, PetUpdateType type) {
        int currentPetCount = ApartmentDao.findPetCountByApartment(apartmentId);
        switch (type) {
            case REMOVE -> currentPetCount--;
            case ADD -> currentPetCount++;
        }

        if (currentPetCount < 0) throw new IllegalArgumentException("Pet count can't be less than 0");
        ApartmentDao.updateApartmentPets(apartmentId, currentPetCount);

        return ApartmentDao.findApartmentDtoById(apartmentId);
    }

    public OwnerDto removeOwnerFromApartment(long apartmentId, long ownerId) {
        Transaction transaction = null;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Apartment apartment = ApartmentDao.findApartmentById(session, apartmentId);
            Owner owner = OwnerDao.findOwnerById(session, ownerId);
            OwnerDto removedOwnerDto = OwnerDao.findOwnerDtoById(session, ownerId);

            if (apartment == null || apartment.isDeleted()) {
                throw new NotFoundException(Apartment.class, apartmentId);
            }

            if (owner == null || owner.isDeleted()) {
                throw new NotFoundException(Owner.class, ownerId);
            }

            if (apartment.getOwners().contains(owner)) {
                apartment.getOwners().remove(owner);
                owner.getApartments().remove(apartment);
                if (owner.getApartments().isEmpty()) OwnerDao.deleteOwner(ownerId);
            } else {
                throw new IllegalStateException("Owner not associated with this apartment.");
            }

            transaction.commit();

            return removedOwnerDto;
        } catch (RuntimeException e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    public OwnerDto addOwnerToApartment(long apartmentId, long ownerId) {
        Transaction transaction = null;

        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            Apartment apartment = ApartmentDao.findApartmentById(session, apartmentId);
            Owner owner = OwnerDao.findOwnerById(session, ownerId);
            OwnerDto addedOwnerDto = OwnerDao.findOwnerDtoById(session, ownerId);

            if (apartment == null || owner == null || apartment.isDeleted() || owner.isDeleted()) {
                throw new EntityNotFoundException("Apartment or owner not found or not active.");
            }

            apartment.getOwners().add(owner);
            owner.getApartments().add(apartment);

            transaction.commit();

            return addedOwnerDto;
        } catch (RuntimeException e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    public void addOccupantToApartment(long apartmentId, @Valid CreateOccupantDto occupantDto) {
        Transaction transaction = null;

        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            Apartment apartment = ApartmentDao.findApartmentById(session, apartmentId);
            if (apartment == null || apartment.isDeleted()) {
                throw new EntityNotFoundException("Active apartment with id " + apartmentId + " not found.");
            }

            OccupantDao.createOccupant(session, occupantDto, apartmentId);

            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    public OccupantDto removeOccupantFromApartment(long apartmentId, long occupantId) {
        Transaction transaction = null;

        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            Apartment apartment = ApartmentDao.findApartmentById(session, apartmentId);
            Occupant occupant = OccupantDao.findOccupantById(session, occupantId);
            OccupantDto deletededOccupantDto = OccupantDao.findOccupantDtoById(session, occupantId);

            if (apartment == null || occupant == null || apartment.isDeleted() || occupant.isDeleted()) {
                throw new EntityNotFoundException("Apartment or occupant not found or active.");
            }

            apartment.getOccupants().remove(occupant);
            OccupantDao.deleteOccupant(occupantId);

            transaction.commit();

            return deletededOccupantDto;
        } catch (RuntimeException e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    public List<ApartmentDto> getAllApartments() {
        return ApartmentDao.findAllApartments();
    }

    public int getApartmentCountByBuilding(long buildingId) {
        return ((int) ApartmentDao.findApartmentCountByBuilding(buildingId));
    }

    public List<ApartmentDto> getAllApartmentsByBuilding(long buildingId) {
        return ApartmentDao.findAllApartmentsByBuilding(buildingId);
    }


}
