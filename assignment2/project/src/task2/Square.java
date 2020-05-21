package task2;

import java.util.ArrayList;
import java.util.List;

import sim.Global;
import sim.Signal.Type;

class Square {

	private Square[] neighbours;
	private List<Student> students;

	public Square(int x, int y) {
		students = new ArrayList<Student>();
	}

	public void setNeighbours(Square[] neighbours) {
		this.neighbours = neighbours;
	}

	public void studentLeave(Student student) {
		students.remove(student);
	}

	public void studentEnter(Student student) {
		Student other = students.get(students.size() - 1);
		students.add(student);

		if (!other.isTalking()) {
			startTalking(student, other);
		}
	}

	public void startTalking(Student s1, Student s2) {
		Global.SendSignal(Type.START_TALKING, s1, 0);
		Global.SendSignal(Type.START_TALKING, s2, 0);
	}
}