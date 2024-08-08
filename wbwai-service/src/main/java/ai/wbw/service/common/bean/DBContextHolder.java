package ai.wbw.service.common.bean;

import ai.wbw.service.common.enums.DBTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class DBContextHolder {

    private static Logger logger = LoggerFactory.getLogger(DBContextHolder.class);

    private static final ThreadLocal<DBTypeEnum> contextHolder = new ThreadLocal<>();

    private static final AtomicInteger counter = new AtomicInteger(-1);

    public static void set(DBTypeEnum dbType) {
        contextHolder.set(dbType);
    }

    public static DBTypeEnum get() {
        return contextHolder.get();
    }

    public static void master() {
        set(DBTypeEnum.MASTER);
        logger.debug("--DataSource 切换到master");
    }

    public static void slave() {
        //  如果有多个slave库，可以轮询。
        /** int index = counter.getAndIncrement() % 2;
        if (counter.get() > 9999) {
            counter.set(-1);
        }
        if (index == 0) {
            set(DBTypeEnum.SLAVE1);
            logger.debug("--DataSource 切换到slave1");
        }else {
            set(DBTypeEnum.SLAVE2);
            logger.debug("--DataSource 切换到slave2");
        } **/
        set(DBTypeEnum.SLAVE1);
        logger.debug("--DataSource 切换到slave1");
    }

}