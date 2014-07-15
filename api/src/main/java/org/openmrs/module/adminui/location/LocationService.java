package org.openmrs.module.adminui.location;

import java.util.List;

import org.openmrs.Location;
import org.openmrs.LocationAttributeType;
import org.openmrs.LocationTag;
import org.openmrs.annotation.Authorized;
import org.openmrs.api.APIException;
import org.openmrs.util.PrivilegeConstants;
import org.springframework.transaction.annotation.Transactional;

public interface LocationService {
	
	/**
	 * Save location to database (create if new or update if changed)
	 *
	 * @param location is the location to be saved to the database
	 * @should throw APIException if location has no name
	 * @should overwrite transient tag if tag with same name exists
	 * @should throw APIException if transient tag is not found
	 * @should return saved object
	 * @should remove location tag from location
	 * @should add location tag to location
	 * @should remove child location from location
	 * @should cascade save to child location from location
	 * @should update location successfully
	 * @should create location successfully
	 */
	@Authorized( { PrivilegeConstants.MANAGE_LOCATIONS })
	public Location saveLocation(Location location) throws APIException;
	
	/**
	 * Returns all locations, includes retired locations. This method delegates to the
	 * #getAllLocations(boolean) method
	 *
	 * @return locations that are in the database
	 * @should return all locations including retired
	 */
	@Transactional(readOnly = true)
	@Authorized( { PrivilegeConstants.VIEW_LOCATIONS })
	public List<Location> getAllLocations() throws APIException;
	
	/**
	 * @return all {@link LocationAttributeType}s
	 * @since 1.9
	 * @should return all location attribute types including retired ones
	 */
	@Transactional(readOnly = true)
	@Authorized(PrivilegeConstants.VIEW_LOCATION_ATTRIBUTE_TYPES)
	List<LocationAttributeType> getAllLocationAttributeTypes();
	
	/**
	 * Returns a location given that locations primary key <code>locationId</code> A null value is
	 * returned if no location exists with this location.
	 *
	 * @param locationId integer primary key of the location to find
	 * @return Location object that has location.locationId = <code>locationId</code> passed in.
	 * @should return null when no location match given location id
	 */
	@Transactional(readOnly = true)
	@Authorized( { PrivilegeConstants.VIEW_LOCATIONS })
	public Location getLocation(Integer locationId) throws APIException;
	
	/**
	 * Returns all location tags, includes retired location tags. This method delegates to the
	 * #getAllLocationTags(boolean) method.
	 *
	 * @return location tags that are in the database
	 * @should return all location tags including retired
	 * @since 1.5
	 */
	@Transactional(readOnly = true)
	@Authorized( { PrivilegeConstants.VIEW_LOCATIONS })
	public List<LocationTag> getAllLocationTags() throws APIException;

}
