def pipeline(dslFactory) {
  dslFactory.with {
    quietPeriod(10)
    concurrentBuild(false)
  }
}
