import java.util.ArrayList;
import java.util.List;

/**
 * A classroom object holding the students and the assignments
 * @author kymed
 *
 */

public class Classroom {

	String classCode, className, teacher; // the unique code of the class, the name of the class and the teacher for it
	List<String> studentUsernames; // a list of the student user names in the classroom
	List<String> studentNames; // a list of the student full names in the assignment
	List<Assignment> assignments = new ArrayList<Assignment>(); // an array list of the assignments in the classroom
	
	/**
	 * Create a new classroom
	 * @param classCode unique classcode (ex: ics4u1)
	 * @param className the full name of the class ( ex: Intro to Computer Science )
	 * @param teacher the teacher for the classroom
	 * @param studentUsernames existing usernames
	 * @param studentNames existing fullnames
	 */
	public Classroom(String classCode, String className, String teacher,ArrayList<String> studentUsernames, ArrayList<String> studentNames) {
		this.teacher = teacher;
		this.classCode = classCode;
		this.className = className;
		this.studentNames = studentNames;
		this.studentUsernames = studentUsernames;
	}
	
	/**
	 * 
	 * @return the unique class code
	 */
	public String getCode() {
		return classCode;
	}
	
	/**
	 * 
	 * @return the full name of the class
	 */
	public String getName() { 
		return className;
	}
	
	/**
	 * 
	 * @return the name of the class's teacher
	 */
	public String getTeacher() {
		return this.teacher;
	}
	
	/**
	 *  Add an assignment to the classroom
	 * @param a the assignment
	 */
	public void addAssignment(Assignment a) {
		a.addStudents(studentUsernames);
		assignments.add(a);
	}
	
	/**
	 * Get the list of all existing assignments
	 * @return the assignments
	 */
	public List<Assignment> getAssignments() { 
		return assignments;
	}
	
	/**
	 * Delete an assignment from the class
	 * @param a the assignment
	 */
	public void delAssignment(Assignment a) {
		assignments.remove(a);
	}
	
	/**
	 * Get a list of the student full names in the class
	 * @return the list of students
	 */
	public List<String> getStudents() {
		return studentNames;
	}
	
	/**
	 * Get a list of the student usernames in the class
	 * @return the usernames
	 */
	public List<String> getStudentUsernames() {
		return studentUsernames;
	}
	
	/**
	 * Organize the students in the assignment, to make sure that the students are ordered
	 * in the order that the class is in, and their index/spot in the arraylist of scores
	 * are the same as their index/spot in their arraylist of names
	 */
	public void organizeStudentsInAssignments() {
		ArrayList<Integer> studentScores = new ArrayList<Integer>();
		
		for (Assignment a : getAssignments()) {
			studentScores = new ArrayList<Integer>();
			
			for (String student : getStudentUsernames()) {
				if(a.getStudents().contains(student)) {
					System.out.println(a.getScore(student));
					studentScores.add(a.getScore(student));
				} else {
					studentScores.add(0);
				}
			}
			
			a.setStudents(getStudentUsernames());
			a.setScores(studentScores);
		}
		
	}
	
	/**
	 * Add a new student to the classroom and add him/her to existing assignments
	 * @param username of the student
	 * @param fullname of the student
	 */
	public void addStudent(String username, String fullname) {
		studentUsernames.add(username);
		studentNames.add(fullname);
		for (Assignment a : assignments) {
			if (!a.getStudents().contains(username)) {
				a.addStudent(username, 0);
			}
		}
	}
	
	/**
	 * Remove a student from the class and all existing assignments
	 * @param username of the student
	 */
	public void delStudent(String username) {
		if (studentUsernames.contains(username)) {
			int userindex = studentUsernames.indexOf(username);
			studentUsernames.remove(userindex);
			studentNames.remove(userindex);
			for (Assignment a : assignments) {
				if (a.getStudents().contains(username)) {
					a.delStudent(username);
				}
			}
		}
	}
	
	/**
	 * Get a certain assignment object based off the name
	 * @param name of the assignment
	 * @return the assignment object itself
	 */
	public Assignment getAssignment(String name) {
		for (Assignment a : assignments) {
			if (a.getName().equals(name)) {
				return a;
			}
		}
		return null;
	}
	
	/**
	 * Edit the exact score of a specific assignment for a specific assignment
	 * @param student 
	 * @param assignment
	 * @param score
	 */
	public void editAssignmentScore(String student, String assignment, int score) {
		if (getAssignment(assignment) != null) {
			if (getAssignment(assignment).getStudents().contains(student)) {
				getAssignment(assignment).changeScore(student, score);
			}
		} else {
			System.out.println("Assignment not found, score could not be edited for: " + student);
		}
	}
	
	/**
	 * Edit the weighting of a certain assignment
	 * @param assignment 
	 * @param weighting
	 */
	public void editAssignmentWeighting(String assignment, int weighting) {
		if (getAssignment(assignment) != null) {
			getAssignment(assignment).setWeight(weighting);
		}
	}
	
	/**
	 * get a student's average
	 * @param student 
	 * @return the average
	 */
	public int getStudentScore(String student) {
		int score = 0;
		for (Assignment a : assignments) {
			if (a.getStudents().contains(student) && a.getScore(student) != null) {
				System.out.println(a.getWeight()/100);
				score += (int) Math.round(a.getScore(student) * ((double)a.getWeight() / (double)100));
			}
		}
		
		return score;
	}
	
	/**
	 * Get the average of all the student averages in the class
	 * @return the average
	 */
	public int getCourseAverage() {
		int score = 0;
		int studentnum = 0;
		for (String student : studentUsernames) {
			score += getStudentScore(student);
			studentnum += 1;
		}
		if (score == 0 || studentnum == 0) {
			score = 0;
		} else {
			score = (int) Math.round((double)score / (double)studentnum);
		}
		return score;
	}
	
	/**
	 * Rewrite the information in the database
	 * @return whether it was successful or not
	 */
	public String applyChanges() {
		
		String delAttempt = Controller.delClass(getCode());
		String writeAttempt = Controller.addClass(this);
		
		/*if (!delAttempt.equals("class successfully removed")) {
			return delAttempt;
		}
		
		String writeAttempt = Controller.addClass(this);
		if (!writeAttempt.equals("Class created successfully")) {
			return writeAttempt;
		}*/
		
		return "changes successfully saved";
	}
	
	
}