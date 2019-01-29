package ourbusinessproject;

import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class EnterpriseProjectService {

    @PersistenceContext
    private EntityManager entityManager;

    public Project saveProjectForEnterprise(Project project, Enterprise enterprise) {
        saveEnterprise(enterprise);
        project.setEnterprise(enterprise);
        enterprise.addProject(project);
        
        if (entityManager.contains(project)) {
        	entityManager.persist(project);
            entityManager.flush();
            return project;
        } else {
        	Project newProject = new Project();
        	newProject.setDescription(project.getDescription());
        	newProject.setEnterprise(project.getEnterprise());
        	newProject.setTitle(project.getTitle());
        	newProject.setVersion(project.getVersion());
        	
        	entityManager.persist(newProject);
            entityManager.flush();
            return newProject;
        }
    }

    public Enterprise saveEnterprise(Enterprise enterprise) {
    	if (entityManager.contains(enterprise)) {
    		entityManager.persist(enterprise);
            entityManager.flush();
            return enterprise;
    	} else {
	    	Enterprise newEnterprise = new Enterprise();
	    	newEnterprise.setName(enterprise.getName());
	    	newEnterprise.setDescription(enterprise.getDescription());
	    	newEnterprise.setContactEmail(enterprise.getContactEmail());
	    	newEnterprise.setContactName(enterprise.getContactName());
	    	newEnterprise.setId(enterprise.getId());
	    	
	        entityManager.flush();
	        return newEnterprise;
    	}
    }

    public Project findProjectById(Long id) {
        return entityManager.find(Project.class, id);
    }

    public Enterprise findEnterpriseById(Long id) {
        return entityManager.find(Enterprise.class, id);
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<Project> findAllProjects() {
        TypedQuery<Project> query = entityManager.createQuery("select p from Project p join fetch p.enterprise order by p.title", Project.class);
        return query.getResultList();
    }
}
