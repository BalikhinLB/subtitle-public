package subtitle.base.startup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import subtitle.base.startup.model.DbUpdate;
import subtitle.base.startup.repository.DbUpdateRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ApplicationStartup implements ApplicationListener<ApplicationReadyEvent> {
    private final List<DbUpdateAfterStart> dbUpdateBeans;
    private final DbUpdateRepository dbUpdateRepository;

    @Autowired
    public ApplicationStartup(List<DbUpdateAfterStart> dbUpdateBeans, DbUpdateRepository dbUpdateRepository) {
        this.dbUpdateBeans = dbUpdateBeans;
        this.dbUpdateRepository = dbUpdateRepository;
    }

    @Override
    public void onApplicationEvent(@NonNull final ApplicationReadyEvent event) {
        for (DbUpdateAfterStart bean : dbUpdateBeans) {
            if (bean.isRepeatable()) {
                runDbUpdate(bean);
            } else {
                if (!dbUpdateRepository.existsByName(bean.getName())) {
                    runDbUpdate(bean);
                }
            }
        }
    }

    private void runDbUpdate(DbUpdateAfterStart bean) {
        bean.dbUpdate();
        DbUpdate dbUpdate = dbUpdateRepository.findFirstByName(bean.getName());
        if (dbUpdate == null) {
            dbUpdate = new DbUpdate(bean.getName(), LocalDateTime.now());
        } else {
            dbUpdate.setCreateDate(LocalDateTime.now());
        }
        dbUpdateRepository.save(dbUpdate);
    }


}