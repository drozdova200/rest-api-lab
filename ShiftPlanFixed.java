import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ShiftPlanFixed {
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
    private final List<String> log = new ArrayList<>();

    public static void main(String[] args) {
        ShiftPlanFixed planner = new ShiftPlanFixed();
        List<Brigade> brigades1 = new ArrayList<>();
        brigades1.add(new Brigade("Иванов", 8.0, true));
        brigades1.add(new Brigade("Петров", 8.0, true));
        brigades1.add(new Brigade("Сидоров", 8.0, true));
        String res1 = planner.make(brigades1, 2);
        check("Тест 1", res1.equals("Иванов; Петров; "));

        List<Brigade> brigades2 = new ArrayList<>();
        brigades2.add(new Brigade("Козлов", 11.0, true));
        brigades2.add(new Brigade("Смирнов", 8.0, false));
        brigades2.add(new Brigade("Новиков", 8.0, true));
        String res2 = planner.make(brigades2, 2);
        check("Тест 2", res2.equals("Новиков; "));

        List<Brigade> brigades3 = new ArrayList<>();
        brigades3.add(new Brigade("Орлов", 8.0, true));
        String res3 = planner.make(brigades3, 5);
        check("Тест 3", res3.equals("Орлов; "));

        try {
            planner.make(null, 2);
            check("Тест 4", false);
        } catch (IllegalArgumentException e) {
            check("Тест 4", true);
        }

        check("Тест 5", planner.log.size() == 3);
        System.out.println("Все проверки пройдены.");
    }

    static void check(String name, boolean passed) {
        System.out.println((passed ? "[OK] " : "[FAIL] ") + name);
        if (!passed) throw new AssertionError("Проверка не пройдена: " + name);
    }

    public String make(List<Brigade> brigades, int need) {
        if (brigades == null) throw new IllegalArgumentException("Список бригад не может быть null");
        if (need < 0) throw new IllegalArgumentException("Потребность не может быть отрицательной");

        List<Brigade> validBrigades = new ArrayList<>();
        for (Brigade b : brigades) {
            if (b.hoursPerShift <= 10.0 && b.admitted) {
                validBrigades.add(b);
            }
        }

        StringBuilder result = new StringBuilder();
        int limit = Math.min(need, validBrigades.size());
        for (int i = 0; i < limit; i++) {
            result.append(validBrigades.get(i).lastName).append("; ");
        }

        String resStr = result.toString();
        try (FileWriter writer = new FileWriter("наряд.txt")) {
            writer.write(resStr);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка записи в файл наряда", e);
        }

        log.add(dateFormat.format(new Date()) + " наряд сформирован");
        return resStr;
    }
}

class Brigade {
    final String lastName;
    final double hoursPerShift;
    final boolean admitted;

    Brigade(String lastName, double hoursPerShift, boolean admitted) {
        this.lastName = lastName;
        this.hoursPerShift = hoursPerShift;
        this.admitted = admitted;
    }
}
