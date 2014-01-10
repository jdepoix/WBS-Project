/*
* The WBS-­Tool is a project managment tool combining the Work Breakdown
* Structure and Earned Value Analysis
* Copyright (C) 2013 FH-­Bingen
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY;; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program. If not, see <http://www.gnu.org/licenses/>.
*/

package dbaccess.models;

import java.util.List;

import dbaccess.data.Employee;

/**The interface for the employee model*/ 
public interface EmployeesModel {

    /**
     * A method to add a new employee to the project.
     * @param employee The employee which is added to the project.
     */
    public void addNewEmployee(Employee employee);
    
    /**
     * A method to get all employees.
     * @return Returns a list with all employees.
     */
    public List<?> getEmployee();
    
    /**
     * A method to get a specific employee.
     * @param login The login for the specific employee.
     * @return Returns the selected employee.
     */
    public Employee getEmployee(String login);
    
    /**
     * A method to get specific employees of the project.
     * @param isLeader True, if the method should return a list with all leaders of the project. False, if the method should return a list with all employees of the project.
     * @returns Returns a list with all leaders if parameter isLeader is true, else returns a list with all employees.
     */
    public List<?> getEmployee(boolean isLeader);
    
    /**
     * A method to update a employee.
     * @param employee The specific employee which is updated.
     */
    public void updateEmployee(Employee employee);
    
    /**
     * A method to delete a specific employee.
     * @param id The id from the employee which is deleted.
     */
    public void deleteEmployee(int id);
    
}
