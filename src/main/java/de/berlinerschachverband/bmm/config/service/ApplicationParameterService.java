package de.berlinerschachverband.bmm.config.service;

import de.berlinerschachverband.bmm.config.data.ApplicationParameter;
import de.berlinerschachverband.bmm.config.data.ApplicationParameterRepository;
import de.berlinerschachverband.bmm.config.data.thymeleaf.SetApplicationParameterData;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ApplicationParameterService {

    private final ApplicationParameterRepository applicationParameterRepository;

    public ApplicationParameterService(ApplicationParameterRepository applicationParameterRepository) {
        this.applicationParameterRepository = applicationParameterRepository;
    }

    @Transactional
    public void setApplicationParameter(SetApplicationParameterData setApplicationParameterData) {
        applicationParameterRepository.findByApplicationParameterKey(setApplicationParameterData.getKey()).ifPresentOrElse(
                applicationParameter ->  {
                    applicationParameter.setApplicationParameterValue(setApplicationParameterData.getValue());
                },
                () -> {
                    ApplicationParameter applicationParameter = new ApplicationParameter();
                    applicationParameter.setApplicationParameterKey(setApplicationParameterData.getKey());
                    applicationParameter.setApplicationParameterValue(setApplicationParameterData.getValue());
                }
        );
    }

    @Transactional(readOnly = true)
    public Optional<String> getApplicationParameter(String key) {
        Optional<ApplicationParameter> applicationParameter = applicationParameterRepository.findByApplicationParameterKey(key);
        if (applicationParameter.isPresent()) {
            return Optional.of(applicationParameter.get().getApplicationParameterValue());
        } else {
            return Optional.empty();
        }
    }

    /**
     * Checks if the given key has a value associated that equals the given value.
     * @param key
     * @param value
     * @return
     */
    public Boolean keyHasValue(@NonNull String key, @NonNull String value) {
        Optional<String> realValue = getApplicationParameter(key);
        return realValue.isPresent() && realValue.equals(value);
    }
}
