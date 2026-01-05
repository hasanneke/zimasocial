UPDATE quartz.qrtz_job_details
SET job_class_name = REPLACE(
        job_class_name,
        'com.zimaberlin.zimasocial',
        'com.zima.zimasocial')
WHERE job_class_name LIKE 'com.zimaberlin.zimasocial%';