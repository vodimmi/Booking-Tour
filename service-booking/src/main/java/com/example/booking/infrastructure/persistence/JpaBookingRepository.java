package com.example.booking.infrastructure.persistence;

import com.example.booking.domain.model.Booking;
import com.example.booking.domain.repository.BookingRepository;
import org.springframework.stereotype.Repository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class JpaBookingRepository implements BookingRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Booking save(Booking booking) {
        if (booking.getId() == null) {
            entityManager.persist(booking);
            return booking;
        }
        return entityManager.merge(booking);
    }

    @Override
    public Optional<Booking> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Booking.class, id));
    }

    @Override
    public List<Booking> findAll() {
        return entityManager.createQuery("SELECT b FROM Booking b", Booking.class)
                .getResultList();
    }

    @Override
    public boolean existsActiveBookingForUserAndTour(Long userId, Long tourId) {
        Long count = entityManager.createQuery(
                "SELECT COUNT(b) FROM Booking b WHERE b.userId = :userId AND b.tourId = :tourId AND b.status IN ('PENDING', 'CONFIRMED')",
                Long.class)
                .setParameter("userId", userId)
                .setParameter("tourId", tourId)
                .getSingleResult();
        return count > 0;
    }

    @Override
    public void updateBookingStatus(Long bookingId, String status) {
        entityManager.createQuery(
                "UPDATE Booking b SET b.status = :status WHERE b.id = :id")
                .setParameter("status", status)
                .setParameter("id", bookingId)
                .executeUpdate();
    }
}