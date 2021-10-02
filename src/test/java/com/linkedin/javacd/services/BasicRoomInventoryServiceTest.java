package com.linkedin.javacd.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.linkedin.javacd.entities.Room;

@SpringBootTest
@ActiveProfiles(profiles = "test")
class BasicRoomInventoryServiceTest {

	@Autowired
	private RoomInventoryService roomInventoryService;

	@Test
	void createRoomTest() {
		Room room = this.roomInventoryService.create(new Room("Piccadilly", 5, "Suite", new BigDecimal("225.00")));
		assertNotNull(room);
		assertEquals("Piccadilly", room.getName());
	}
	
	@Test
	void findByRoomIdTest() {
		Room room = this.roomInventoryService.findById(1L).get();
		assertNotNull(room);
		assertEquals("Oxford", room.getName());
	}

	@Test
	void findByTypeTest() {
		List<Room> suites = this.roomInventoryService.findByType("Suite");
		List<Room> premiere = this.roomInventoryService.findByType("Premiere Room");
		List<Room> guest = this.roomInventoryService.findByType("Guest Room");
		
		assertEquals(3, suites.size());
		assertEquals(1, premiere.size());
		assertEquals(3, guest.size());
	}

	@Test
	void applyDiscountTest() {
		
		this.roomInventoryService.applyDiscount(10);
		
		List<Room> suites = this.roomInventoryService.findByType("Suite");
		assertEquals(new BigDecimal("229.50"), suites.get(0).getRate());
		assertEquals(new BigDecimal("202.50"), suites.get(1).getRate());
		
		
		Room room = this.roomInventoryService.findById(5L).get();
		assertEquals(new BigDecimal("139.50"), room.getRate());
		
		Room newLondon = this.roomInventoryService.create(new Room("New London", 3, "Guest Room", new BigDecimal("175.0")));
		assertEquals(new BigDecimal("157.50"), newLondon.getRate());
	}

	@Test
	void deleteTest() {
		Room room = this.roomInventoryService.findById(1L).get();
		this.roomInventoryService.delete(room.getRoomId());
		Optional<Room> result = this.roomInventoryService.findById(room.getRoomId());
		
		assertTrue(result.isEmpty());
	}

}
