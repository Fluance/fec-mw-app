package net.fluance.cockpit.app.service.security;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.fluance.app.data.model.identity.UserProfile;
import net.fluance.cockpit.core.model.jdbc.note.NoteCategory;
import net.fluance.cockpit.core.repository.jdbc.note.roleCategory.NoteCategoriesDao;

@Service
public class NotesRolesCategoriesService {

	@Autowired
	private NoteCategoriesDao roleCategoriesDao;
	private Map<String, List<Integer>> roleCategories;

	public boolean hasCategory(String role, Integer categoryToCheck) {
		this.roleCategories = roleCategoriesDao.getAllRoleCategories();
		if (roleCategories.get(role) != null && roleCategories.get(role).contains(categoryToCheck))
			return true;
		else
			return false;
	}

	/**
	 * Check if one of the roles of the {@link UserProfile} has the category passed as argument
	 * 
	 * @param userProfile
	 * @param categoryToCheck
	 * @return
	 */
	public boolean hasAccessToCategory(UserProfile userProfile, Integer categoryToCheck) {
		if (userProfile.getProfile() == null || userProfile.getProfile().getGrants() == null || userProfile.getProfile().getGrants().getRoles() == null) {
			return false;
		}
		List<String> roles = userProfile.getProfile().getGrants().getRoles();
		for (String role : roles) {
			if (hasCategory(role, categoryToCheck))
				return true;
		}
		return false;
	}

	public void setRoleCategories(Map<String, List<Integer>> roleCategories) {
		this.roleCategories = roleCategories;
	}

	public List<Integer> getAccessibleCategoriesIds(UserProfile userProfile) {
		if (userProfile == null || userProfile.getProfile() == null || userProfile.getProfile().getGrants() == null || userProfile.getProfile().getGrants().getRoles() == null) {
			return new ArrayList<>();
		}
		List<String> roles = userProfile.getProfile().getGrants().getRoles();
		return roleCategoriesDao.getCategoriesForRoles(roles);
	}

	public List<NoteCategory> getAccessibleCategories(UserProfile userProfile) {
		if (userProfile == null || userProfile.getProfile() == null || userProfile.getProfile().getGrants() == null || userProfile.getProfile().getGrants().getRoles() == null) {
			return new ArrayList<>();
		}
		List<String> roles = userProfile.getProfile().getGrants().getRoles();
		return roleCategoriesDao.getCategoriesDetails(roles);
	}
}