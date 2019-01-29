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
        
        /* Le test testSaveDetachedProject passe avec le bloc de code suivant décommenté.
         * Cependant, l'objet n'est pas persistent, ce qui empêche les tests de la partie 2 et 3 de passer.
         * if (!entityManager.contains(project)) {
        	Project newProject = new Project();
        	newProject.setDescription(project.getDescription());
        	newProject.setEnterprise(project.getEnterprise());
        	newProject.setTitle(project.getTitle());
        	newProject.setVersion(project.getVersion());
        	
            entityManager.flush();
            return newProject;
        }*/
        
        entityManager.persist(project);
        entityManager.flush();
        return project;
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
