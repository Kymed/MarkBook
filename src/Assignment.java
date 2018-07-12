import java.util.ArrayList;
import java.util.List;
/**
 * This class can hold objects of an assignments information
 * @author kymed
 *
 */
public class Assignment {
	
	private int weight; // weight of an assignment
	private String name; // name of an assignment
	private List<String> students = new ArrayList<String>(); // students in the assignment
	private List<Integer> studentScores = new ArrayList<Integer>(); // scores for the students
	
	/**
	 * Creates a new assignment
	 * @param weight : of the assignment
	 * @param name : name of the assignment
	 */
	public Assignment(int weight, String name) {
		this.weight = weight;
		this.name = name;
	}
	
	/**
	 * 
	 * @return the weight of the assignment
	 */
	public int getWeight() {
		return this.weight;
	}
	
	/**
	 * 
	 * @param weight : the weight to set the assignment as
	 */
	public void setWeight(int weight) {
		this.weight = weight;
	}
	
	/**
	 * 
	 * @return the name of the assignment
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * 
	 * @return the scores in the assignment
	 */
	public List<Integer> scores() {
		return this.studentScores;
	}
	
	/**
	 * returns the score of a specific assignment
	 * @param student
	 * @return score
	 */
	public Integer getScore(String student) {
		if (students.contains(student)) {
			int index = students.indexOf(student);
			return studentScores.get(index);
		}
		return -1;
	}
	
	/**
	 * 
	 * @return list of scores
	 */
	public List<Integer> getScores() {
		return studentScores;
	}
	
	/**
	 * 
	 * @return list of students
	 */
	public List<String> getStudents() {
		return students;
	}
	
	/**
	 * change the score that a specific student got
	 * @param student
	 * @param score
	 */
	public void changeScore(String student, int score) {
		int index = students.indexOf(student);
		if (studentScores.size() < index) {
			System.out.println("db failure, assign: " + getName());
		} else {
			studentScores.set(index, score);
		}
	}
	
	/**
	 * Remove a student from the assignment
	 * @param student
	 */
	public void delStudent(String student) {
		int index = students.indexOf(student);
		if (studentScores.size() < index || students.size() < 0) {
			System.out.println("db failure, assign: " + getName());
		} else {
			students.remove(index);
			studentScores.remove(index);
		}
	}
	
	/**
	 * Add a student to the assignment
	 * @param student
	 * @param score
	 */
	public void addStudent(String student, int score) {
		if (!students.contains(student)) {
			students.add(student);
			studentScores.add(score);
		}
	}
	
	/**
	 * Add a list of students to the assignment
	 * @param students
	 */
	public void addStudents(List<String> students) {
		for (String student : students) {
			if (!this.students.contains(student)) {
				this.students.add(student);
				this.studentScores.add(0);
			}
		}
	}
	
	/**
	 * Set the students of the assignment
	 * @param students2
	 */
	public void setStudents(List<String> students2) {
		students = students2;
	}
	
	/**
	 * Set the scores of the assignment
	 * @param studentScores2 : the scores
	 */
	public void setScores(ArrayList<Integer> studentScores2) {
		this.studentScores = studentScores2;
	}
}
