package task2;

import java.util.ArrayList;
import java.util.List;

import sim.Global;
import sim.Signal.Type;
import task2.Student.Direction;

class Square {

	private Square[] neighbours;
	private List<Student> students;

	public Square() {
		students = new ArrayList<Student>();
	}

	public void setNeighbours(Square[] neighbours) {
		this.neighbours = neighbours;
	}

	public Square next(Direction dir) {
		Direction[] dirs = Direction.values();
		for (int i = 0; i < dirs.length; i++) {
			if (dirs[i].equals(dir)) {
				return neighbours[i];
			}
		}
		return null;
	}

	public void studentLeave(Student student) {
		if (student.isTalking()) {
			System.err.println("Student tried to leave while talking!");
		}
		students.remove(student);
	}

	public void studentEnter(Student student) {
		students.add(student);
		if (students.size() > 1) {
			Student other = students.get(students.size() - 2);

			if (!other.isTalking()) {
				startTalking(student, other);
			}
		}
	}

	public void startTalking(Student s1, Student s2) {
		Data.meetings++;
		Global.SendSignal(Type.START_TALKING, s1, 0);
		Global.SendSignal(Type.START_TALKING, s2, 0);
	}
}