def PROJECT_ROOT = "jobs/seed"
try {
  PROJECT_ROOT = "${gradle_project_dir}/jobs/seed"
} catch(e){
  PROJECT_ROOT = "jobs/seed"
}

job_publisher_job = [
  name:"job-publisher"+(flavour ? "-${flavour}" : ""),
  dsl:(String)"${PROJECT_ROOT}/dsl/job-publisher.groovy",
  pipeline:[
      (String)"${PROJECT_ROOT}/pipeline/job-publisher.groovy"
  ],
]

job_publisher_stable_job = [
  name:"job-publisher"+(flavour ? "-${flavour}" : "")+"-stable",
  dsl:(String)"${PROJECT_ROOT}/dsl/job-publisher.groovy",
  pipeline:[
      (String)"${PROJECT_ROOT}/pipeline/job-publisher.groovy"
  ],
]

return [
  name:"seed",
  jobs:[job_publisher_job,job_publisher_stable_job],
  test_suites:[],
]
