def pipeline(dslFactory,seed_ref,job) {

  if (seed_ref =~ /master/) {
    _quietPeriod = 10
  } else {
    _quietPeriod = 1
  }

  dslFactory.with {
    quietPeriod(_quietPeriod)
    concurrentBuild(true)
  }
}
