package org.springframework.base.system.service;

import org.springframework.base.system.dao.PersonDao;
import org.springframework.base.system.entity.Person;
import org.springframework.base.system.persistence.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PersonService {
    @Autowired
    private PersonDao personDao;

    public PersonService() {
    }

    public Page<Map<String, Object>> personList(Page<Map<String, Object>> page, String username, String realname) throws Exception {
        return this.personDao.personList(page, username, realname);
    }

    public boolean personUpdate(Person person) throws Exception {
        return this.personDao.personUpdate(person);
    }

    public boolean deletePerson(String[] ids) throws Exception {
        return this.personDao.deletePerson(ids);
    }

    public Map<String, Object> getPerson(String id) throws Exception {
        return this.personDao.getPerson(id);
    }

    public List<Map<String, Object>> selectPersonByIds(String[] ids) {
        return this.personDao.selectPersonByIds(ids);
    }

    public boolean userNameIsExists(String userName) throws Exception {
        return this.personDao.userNameIsExists(userName);
    }

    public boolean personUpdateDatascope(Person person) throws Exception {
        return this.personDao.personUpdateDatascope(person);
    }

}
