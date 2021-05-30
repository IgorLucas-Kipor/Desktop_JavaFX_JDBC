package model.services;

import java.util.ArrayList;
import java.util.List;

import model.entities.Department;

public class DepartmentService {
	
	
	
	public List<Department> findAll() {
		List<Department> list = new ArrayList<>();
		Department department1 = new Department(1, "Books");
		Department department2 = new Department(2, "Music");
		Department department3 = new Department(3, "Games");
		list.add(department1);
		list.add(department2);
		list.add(department3);
		return list;
	}

}
