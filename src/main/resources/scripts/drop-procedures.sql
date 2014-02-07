DROP PROCEDURE IF EXISTS dependencies_select;
DROP PROCEDURE IF EXISTS dependencies_delete_by_key;
DROP PROCEDURE IF EXISTS dependencies_new;
DROP PROCEDURE IF EXISTS analyse_data_select_by;
DROP PROCEDURE IF EXISTS analyse_data_new;
DROP PROCEDURE IF EXISTS workpackage_select;
DROP PROCEDURE IF EXISTS workpackage_select_by_id;
DROP PROCEDURE IF EXISTS workpackage_select_by_date;
DROP PROCEDURE IF EXISTS workpackage_new;
DROP PROCEDURE IF EXISTS workpackage_update_by_id;
DROP PROCEDURE IF EXISTS workpackage_delete_by_id;
DROP PROCEDURE IF EXISTS work_effort_select;
DROP PROCEDURE IF EXISTS work_effort_select_sum;
DROP PROCEDURE IF EXISTS work_effort_new;
DROP PROCEDURE IF EXISTS baseline_select;
DROP PROCEDURE IF EXISTS baseline_new;
DROP PROCEDURE IF EXISTS holidays_calendar_select;
DROP PROCEDURE IF EXISTS holidays_calendar_select_by_date;
DROP PROCEDURE IF EXISTS holidays_calendar_new;
DROP PROCEDURE IF EXISTS holidays_calendar_update_by_id;
DROP PROCEDURE IF EXISTS holidays_calendar_delete_by_id;
DROP PROCEDURE IF EXISTS conflicts_select;
DROP PROCEDURE IF EXISTS conflicts_new;
DROP PROCEDURE IF EXISTS conflicts_delete;
DROP PROCEDURE IF EXISTS conflicts_delete_by_id;
DROP PROCEDURE IF EXISTS semaphore_p;
DROP PROCEDURE IF EXISTS semaphore_v;
DROP PROCEDURE IF EXISTS semaphore_select;
DROP PROCEDURE IF EXISTS employee_calendar_select;
DROP PROCEDURE IF EXISTS employee_calendar_select_by_date;
DROP PROCEDURE IF EXISTS employee_calendar_select_by_date_and_emp;
DROP PROCEDURE IF EXISTS employee_calendar_new;
DROP PROCEDURE IF EXISTS employee_calendar_update_by_id;
DROP PROCEDURE IF EXISTS employee_calendar_delete_by_id;
DROP PROCEDURE IF EXISTS employees_select;
DROP PROCEDURE IF EXISTS employees_select_by_login;
DROP PROCEDURE IF EXISTS employees_select_by_id;
DROP PROCEDURE IF EXISTS employees_new;
DROP PROCEDURE IF EXISTS employees_update_password_by_id;
DROP PROCEDURE IF EXISTS employees_update_by_id;
DROP PROCEDURE IF EXISTS employees_delete_by_id;
DROP PROCEDURE IF EXISTS wp_allocation_select;
DROP PROCEDURE IF EXISTS wp_allocation_new;
DROP PROCEDURE IF EXISTS wp_allocation_delete_by_key;
DROP PROCEDURE IF EXISTS wp_alloc_workpackage_select;
DROP PROCEDURE IF EXISTS planned_value_select_by_date;
DROP PROCEDURE IF EXISTS planned_value_select_by_wp_and_date;
DROP PROCEDURE IF EXISTS planned_value_new;
DROP PROCEDURE IF EXISTS planned_value_update_by_wp_and_date;
DROP PROCEDURE IF EXISTS planned_value_delete;
DROP PROCEDURE IF EXISTS planned_value_delete_by_wp;
DROP PROCEDURE IF EXISTS project_select;
DROP PROCEDURE IF EXISTS project_new;