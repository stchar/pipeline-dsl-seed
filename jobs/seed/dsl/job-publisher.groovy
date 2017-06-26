def pipeline(dslFactory,seed_ref,job) {
  dslFactory.with {
    quietPeriod(10)
    concurrentBuild(false)
  }
}
