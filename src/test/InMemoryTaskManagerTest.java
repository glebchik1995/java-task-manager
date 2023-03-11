import manager.InMemoryTaskManager;
import manager.Managers;
import org.junit.jupiter.api.BeforeEach;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    protected void beforeEach() {
        manager = new InMemoryTaskManager();
    }
}
