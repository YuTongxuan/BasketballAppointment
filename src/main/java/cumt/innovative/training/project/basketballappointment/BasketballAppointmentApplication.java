package cumt.innovative.training.project.basketballappointment;

import cumt.innovative.training.project.basketballappointment.logger.Logger;
import cumt.innovative.training.project.basketballappointment.utils.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class BasketballAppointmentApplication {

    public static boolean runServer = true;

    public static void main(String[] args) {
        MainUtil.preHandleForServerRun();
        if (runServer) {
            SpringApplication.run(BasketballAppointmentApplication.class, args);
        }
        try {
            Thread.currentThread().join();
        } catch (Exception ex) {
            Logger.log(new Throwable(), Logger.DANGER, ExceptionUtil.getSimpleMessage(ex));
        } finally {
            try {
                DatabaseUtil.close();
                Logger.log(new Throwable(), Logger.INFO, "Database closed.");
            } catch (Exception ex) {
                Logger.log(new Throwable(), Logger.DANGER, ExceptionUtil.getSimpleMessage(ex));
            }
        }
    }
}
