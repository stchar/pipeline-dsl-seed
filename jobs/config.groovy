job_publisher = [
  name:'job-publisher'+(flavour ? "-${flavour}" : ""),
  dsl:'jobs/seed/dsl/job-publisher.groovy',
  pipeline:'jobs/seed/pipeline/job-publisher.groovy',
]

seed = [
  name:'seed',
  jobs:[job_publisher],
  test_suites:[]
]

template_job = [
  name:'template-job',
  dsl:'jobs/template/dsl/template.groovy',
  pipeline:'jobs/template/pipeline/template.groovy'
]

template = [
  name:'template',
  jobs:[template_job],
  test_suites: [
    'jobs/template/test/template_SUITE.groovy',
  ]
]
projects = [seed, template]

test_suites = []
jobs = []

for (prj in projects) {
  // TODO generate only jobs that changed
  // idea is to use gradle as it can analyze dependencies
  for (job in prj.jobs) {
    jobs.add(job)
  }

  for (test_suite in prj.test_suites) {
    test_suites.add(test_suite)
  }
}

return this
