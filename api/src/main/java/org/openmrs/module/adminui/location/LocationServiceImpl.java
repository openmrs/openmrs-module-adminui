package org.openmrs.module.adminui.location;

import java.util.List;

import org.openmrs.Location;
import org.openmrs.LocationAttributeType;
import org.openmrs.LocationTag;
import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.api.db.LocationDAO;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.customdatatype.CustomDatatypeUtil;
import org.springframework.util.StringUtils;

public class LocationServiceImpl extends BaseOpenmrsService implements LocationService {
	
	private LocationDAO dao;
	
	/**
	 * @see org.openmrs.api.LocationService#setLocationDAO(org.openmrs.api.db.LocationDAO)
	 */
	public void setLocationDAO(LocationDAO dao) {
		this.dao = dao;
	}
	
	/**
	 * @see org.openmrs.api.LocationService#saveLocation(org.openmrs.Location)
	 */
	public Location saveLocation(Location location) throws APIException {
		if (location.getName() == null) {
			throw new APIException("Location name is required");
		}
		
		// Check for transient tags. If found, try to match by name and overwrite, otherwise throw exception.
		if (location.getTags() != null) {
			for (LocationTag tag : location.getTags()) {
				
				// only check transient (aka non-precreated) location tags
				if (tag.getLocationTagId() == null) {
					if (!StringUtils.hasLength(tag.getName()))
						throw new APIException("A tag name is required");
					
					LocationTag existing = Context.getLocationService().getLocationTagByName(tag.getName());
					if (existing != null) {
						location.removeTag(tag);
						location.addTag(existing);
					} else
						throw new APIException("Cannot add transient tags! "
						        + "Save all location tags to the database before saving this location");
				}
			}
		}
		
		CustomDatatypeUtil.saveAttributesIfNecessary(location);
		
		return dao.saveLocation(location);
	}
	
	/**
	 * @see org.openmrs.api.LocationService#getAllLocations()
	 */
	public List<Location> getAllLocations() throws APIException {
		return dao.getAllLocations(true);
	}
	
	/**
	 * @see org.openmrs.api.LocationService#getAllLocationAttributeTypes()
	 */
	@Override
	public List<LocationAttributeType> getAllLocationAttributeTypes() {
		return dao.getAllLocationAttributeTypes();
	}
	
	/**
	 * @see org.openmrs.api.LocationService#getLocation(java.lang.Integer)
	 */
	public Location getLocation(Integer locationId) throws APIException {
		return dao.getLocation(locationId);
	}
	
	/**
	 * @see org.openmrs.api.LocationService#getAllLocationTags()
	 */
	public List<LocationTag> getAllLocationTags() throws APIException {
		return dao.getAllLocationTags(true);
	}

}
