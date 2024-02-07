package de.aclue.blog.springwithcrac;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.SmartLifecycle;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

@Configuration
public class CracConfiguration {

    @Bean
    CracLifecyclePublisher cracLifecyclePublisher() {
        return new CracLifecyclePublisher();
    }

    @Slf4j
    static class CracLifecyclePublisher implements ApplicationContextAware, SmartLifecycle {
        public static final int SMART_LIFECYCLE_PHASE = SmartLifecycle.DEFAULT_PHASE - 1024;
        private ApplicationContext applicationContext;
        private volatile boolean running;

        @Override
        public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
            this.applicationContext = applicationContext;
        }

        @Override
        public void start() {
            try {
                log.info("crac start, running:{}", this.running);
                this.running = true;
                this.applicationContext
                        .publishEvent(new CracStartupApplicationEvent());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void stop() {
            try {
                log.info("crac stop, running:{}", this.running);
                this.applicationContext
                        .publishEvent(new CracStopApplicationEvent());
                this.running = false;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public boolean isRunning() {
            return this.running;
        }

        @Override
        public int getPhase() {
            return SMART_LIFECYCLE_PHASE;
        }
    }

    public static class CracStartupApplicationEvent extends ApplicationEvent {

        public CracStartupApplicationEvent() {
            super("crac started");
        }

    }

    public static class CracStopApplicationEvent extends ApplicationEvent {

        public CracStopApplicationEvent() {
            super("crac stopped");
        }

    }
}