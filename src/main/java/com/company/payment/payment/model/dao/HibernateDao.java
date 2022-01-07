package com.company.payment.payment.model.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository(value = "hibernateDao")
public class HibernateDao<T extends Serializable> {

	@Autowired
	protected SessionFactory sessionFactory;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public Integer save(T param) {
		validate(param);
		return (Integer) sessionFactory.getCurrentSession().save(param);
	}

	public void saveOrUpdate(T param) {
		validate(param);
		sessionFactory.getCurrentSession().saveOrUpdate(param);
	}

	public void delete(T param) {
		sessionFactory.getCurrentSession().delete(param);
	}

	public void update(T param) {
		sessionFactory.getCurrentSession().update(param);
	}

	public Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

	public void flush() {
		getCurrentSession().flush();
	}

	public T get(Integer id, Class<T> type) {
		return (T) sessionFactory.getCurrentSession().get(type, id);
	}

	public void validate(T param) throws ConstraintViolationException {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<T>> violations = validator.validate(param);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
	}

	@SuppressWarnings("unchecked")
	public List<T> findAll(Class<T> type) {
		return (List<T>) getCurrentSession().createQuery("from " + type.getName()).list();
	}

}
