package learning;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ReadLearningData {
    public String path;

    public final String controlsDir = "AD_controls_corr_mats";
    public final String controlsIntimeDir = "AD_controls_corr_mats_intime";
    public final String patientsDir = "AD_patients_corr_mats";
    public final String patientsIntimeDir = "AD_patients_corr_mats_intime";

    List<String> controls;
    List<String> controlsIntime;
    List<String> patients;
    List<String> patientsIntime;

    public ReadLearningData() {
        this("data/");
    }

    public ReadLearningData(String path) {
        this.path = path;
        getFiles();
    }

    public void getFiles() {
        controls = getFiles(controlsDir);
        controlsIntime = getFiles(controlsIntimeDir);
        patients = getFiles(patientsDir);
        patientsIntime = getFiles(patientsIntimeDir);
    }


    public List<String> getFiles(String dir) {
        try (Stream<Path> walk = Files.walk(Paths.get(path + dir))) {
            List<String> lst = walk.filter(Files::isRegularFile)
                    .map(Path::toString).collect(Collectors.toList());
            lst.forEach(System.out::println);
            return lst;
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

}
