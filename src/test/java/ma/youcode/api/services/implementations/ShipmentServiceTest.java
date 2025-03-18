package ma.youcode.api.services.implementations;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import jakarta.persistence.EntityNotFoundException;
import ma.youcode.api.enums.ShipmentStatus;
import ma.youcode.api.exceptions.InvalidShipmentStateException;
import ma.youcode.api.exceptions.UnauthorizedShipmentAccessException;
import ma.youcode.api.models.shipments.Shipment;
import ma.youcode.api.models.shipments.ShipmentItem;
import ma.youcode.api.models.users.Customer;
import ma.youcode.api.models.users.User;
import ma.youcode.api.models.users.UserSecurity;
import ma.youcode.api.payloads.requests.ShipmentRequest;
import ma.youcode.api.payloads.responses.ShipmentResponse;
import ma.youcode.api.repositories.ShipmentRepository;
import ma.youcode.api.services.ImageService;
import ma.youcode.api.services.ShipmentService;
import ma.youcode.api.utilities.mappers.ShipmentMapper;
import ma.youcode.api.utilities.shared.Dimensions;
import ma.youcode.api.utilities.shared.Location;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.swing.text.html.Option;
import java.util.*;

@ExtendWith(MockitoExtension.class)
public class ShipmentServiceTest {

    private static final Logger log = LogManager.getLogger(ShipmentServiceTest.class);
    @Mock
    private ShipmentMapper shipmentMapper;

    @Mock
    private ShipmentRepository shipmentRepository;

    @Mock
    private ImageService imageService;

    @Mock
    private Authentication authentication;


    @InjectMocks
    private ShipmentServiceImpl shipmentService;

    @Mock
    private SecurityContext securityContext;

    private  Shipment shipment;
    private  Customer customer;
    @Mock
    private UserSecurity userSecurity;

    @BeforeEach
    public void setup() {



        shipment = new Shipment();
        Dimensions dimensions = Dimensions.builder().height(0.00).width(0.00).length(0.00).build();
        Location arrival = Location.builder().name("Safi").lon(-7.6153131).lat(33.5738331).build();
        Location departure = Location.builder().name("Casablanca").lon(-8.018436312665823).lat(31.60299452731022).build();
        Set<ShipmentItem> items = new HashSet<>();
        items.add(ShipmentItem.builder().id(UUID.randomUUID()).shipment(shipment).dimensions(dimensions).build());
        shipment.setItems(items);
        UUID shipmentId = UUID.randomUUID();
        shipment.setId(shipmentId);
        shipment.setShipmentStatus(ShipmentStatus.PENDING);
        shipment.setArrival(arrival);
        shipment.setDeparture(departure);
        UUID customerId = UUID.randomUUID();
        customer = new Customer();
        customer.setId(customerId);

    }

    @Test
    public void shouldCreateShipmentSuccessfully() {
        // Arrange
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userSecurity);
        when(userSecurity.getId()).thenReturn(customer.getId());

        ShipmentRequest request = ShipmentRequest.builder().build();

        Shipment expectedShipment = new Shipment();
        when(shipmentMapper.fromRequestDTO(any(ShipmentRequest.class))).thenReturn(expectedShipment);
        when(shipmentRepository.save(any(Shipment.class))).thenReturn(expectedShipment);
        when(shipmentMapper.toResponseDTO(any(Shipment.class))).thenReturn(ShipmentResponse.builder().build());

        ShipmentServiceImpl shipmentServiceSpy = Mockito.spy(shipmentService);

        doNothing().when(shipmentServiceSpy).calculateDistance(any(Shipment.class));
        doNothing().when(shipmentServiceSpy).calculateShipmentPrice(any(Shipment.class));
        doNothing().when(shipmentServiceSpy).attachShipmentItems(any(Shipment.class));

        // Act
        ShipmentResponse response = shipmentServiceSpy.create(request);

        // Assert
        assertNotNull(response);
        verify(shipmentMapper).fromRequestDTO(any(ShipmentRequest.class));
        verify(shipmentRepository).save(expectedShipment);
    }

    @Test
    public void testGetAuthUser() {
        // Create a mock of the UserSecurity class (your custom class)
        UserSecurity mockUser = mock(UserSecurity.class);
        // Optionally, set some mock behavior on the UserSecurity class
        when(mockUser.getUsername()).thenReturn("testuser");

        // Mock Authentication object
        Authentication mockAuthentication = mock(Authentication.class);
        when(mockAuthentication.getPrincipal()).thenReturn(mockUser);

        // Mock SecurityContext and set the Authentication
        SecurityContext mockSecurityContext = mock(SecurityContext.class);
        when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);

        // Set the mock SecurityContext in the SecurityContextHolder
        SecurityContextHolder.setContext(mockSecurityContext);

        // Now call your method and assert the behavior
        UserSecurity user = shipmentService.getAuthUser();
        assertNotNull(user);
        assertEquals("testuser", user.getUsername());
    }

    @Test
    public void shouldUpdateShipmentSuccess() {

//        Arrange
        UUID shipmentId = UUID.randomUUID();
        when(shipmentRepository.findById(shipmentId)).thenReturn(Optional.of(shipment));

        ShipmentRequest request = ShipmentRequest.builder().build();

        ShipmentServiceImpl shipmentServiceSpy = Mockito.spy(shipmentService);

        doAnswer(invocation -> {
            Shipment shipmentArg = invocation.getArgument(0); // Use index 0
            shipmentArg.getItems().forEach(item -> {
                imageService.delete(item.getImageURL());
            });
            shipmentArg.getItems().clear();
            return null;
        }).when(shipmentServiceSpy).detachShipmentItems(any(Shipment.class));

        doNothing().when(shipmentMapper).updateEntity(any(ShipmentRequest.class), any(Shipment.class));

        doAnswer(invocation -> {
            shipment = invocation.getArgument(0);

            shipment.getItems().forEach(item -> {
                item.setShipment(shipment);
                item.setImageURL(imageService.uploadImage(item.getImage()));
                item.calculateVolume();
            });
            return null;
        }).when(shipmentServiceSpy).attachShipmentItems(any(Shipment.class));

        doNothing().when(shipmentServiceSpy).hasPermission(any(Shipment.class) , anyString());
        doNothing().when(shipmentServiceSpy).calculateDistance(any(Shipment.class));
        doNothing().when(shipmentServiceSpy).calculateShipmentPrice(any(Shipment.class));
        when(shipmentRepository.save(any(Shipment.class))).thenReturn(shipment);
        when(shipmentMapper.toResponseDTO(any(Shipment.class))).thenReturn(ShipmentResponse.builder().build());



        // Act
        ShipmentResponse response = shipmentServiceSpy.update(shipmentId, request);

        // Assert
        assertNotNull(response);
        verify(shipmentRepository).findById(shipmentId);
        verify(shipmentServiceSpy).detachShipmentItems(shipment);
        verify(shipmentMapper).updateEntity(any(ShipmentRequest.class), any(Shipment.class));
        verify(shipmentServiceSpy).attachShipmentItems(shipment);
        verify(shipmentServiceSpy).calculateDistance(shipment);
    }


    @Test
    public void shouldUpdateShipmentFailedAndThrowsException() {

//        Arrange
        UUID shipmentId = UUID.randomUUID();
        when(shipmentRepository.findById(shipmentId)).thenReturn(Optional.empty());

        ShipmentRequest request = ShipmentRequest.builder().build();

//        Act
        assertThrows(EntityNotFoundException.class , () -> {
            shipmentService.update(shipmentId, request);
        });

    }

    @Test
    public void shouldUpdateShipmentFailedBecauseIsNotOwnerShipment() {

        //Arrange
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userSecurity);
        when(userSecurity.getId()).thenReturn(customer.getId());
        ShipmentRequest request = ShipmentRequest.builder().build();
        UUID shipmentId = UUID.randomUUID();

        when(shipmentRepository.findById(shipmentId)).thenReturn(Optional.of(shipment));
        shipment.setCustomer(customer);
        ShipmentServiceImpl shipmentServiceSpy = spy(shipmentService);
        when(shipmentServiceSpy.isOwnership(shipment)).thenReturn(false);

        assertThrows(UnauthorizedShipmentAccessException.class, () -> {
            shipmentServiceSpy.update(shipmentId, request);
        });

    }
    @Test
    public void shouldUpdateShipmentFailedBecauseShipmentIsNotPending(){

        //Arrange
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userSecurity);
        when(userSecurity.getId()).thenReturn(customer.getId());
        ShipmentRequest request = ShipmentRequest.builder().build();
        UUID shipmentId = UUID.randomUUID();

        when(shipmentRepository.findById(shipmentId)).thenReturn(Optional.of(shipment));
        shipment.setCustomer(customer);
        ShipmentServiceImpl shipmentServiceSpy = spy(shipmentService);
        when(shipmentServiceSpy.isPending(shipment)).thenReturn(false);

        assertThrows(InvalidShipmentStateException.class, () -> {
            shipmentServiceSpy.update(shipmentId, request);
        });

    }
}