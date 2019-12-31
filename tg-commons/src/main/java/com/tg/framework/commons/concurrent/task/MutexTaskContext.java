package com.tg.framework.commons.concurrent.task;

import java.util.List;

public interface MutexTaskContext {

  List<MutexTaskJob> getPreparingJobs();

  List<MutexTaskJob> getMainJobs();

  List<MutexTaskJob> getFinishingJobs();

}
