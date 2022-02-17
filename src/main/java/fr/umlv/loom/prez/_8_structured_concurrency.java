package fr.umlv.loom.prez;

import jdk.incubator.concurrent.StructuredTaskScope;

public interface _8_structured_concurrency {
  private static void simple() throws InterruptedException {
    try (var scope = new StructuredTaskScope<>()) {
      var start = System.currentTimeMillis();
      var future1 = scope.fork(() -> {
        Thread.sleep(1_000);
        return 42;
      });
      var future2 = scope.fork(() -> {
        Thread.sleep(1_000);
        return 100;
      });
      scope.join();
      var end = System.currentTimeMillis();
      System.out.println("elapsed " + (end - start));
      var result = future1.resultNow() + future2.resultNow();
      System.out.println(result);
    } // call close() !
  }

  private static void runningTask1() throws InterruptedException {
    try (var scope = new StructuredTaskScope<>()) {
      var start = System.currentTimeMillis();
      var future1 = scope.fork(() -> {
        Thread.sleep(1_000);
        return 42;
      });
      var future2 = scope.fork(() -> {
        Thread.sleep(1_000);
        System.out.println("end");
        return 100;
      });
      scope.join();
      var end = System.currentTimeMillis();
      System.out.println("elapsed " + (end - start));
      //var result = future1.resultNow() + future2.resultNow();
      var result = future1.resultNow();
      System.out.println(result);
    } // call close() !
  }

  private static void runningTask2() throws InterruptedException {
    try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
      var start = System.currentTimeMillis();
      var future1 = scope.<Integer>fork(() -> {
        throw new AssertionError("oops");
      });
      var future2 = scope.fork(() -> {
        Thread.sleep(1_000);
        System.out.println("end");
        return 42;
      });
      scope.join();
      var end = System.currentTimeMillis();
      System.out.println("elapsed " + (end - start));
      var result = future1.resultNow() + future2.resultNow();
      System.out.println(result);
    }
  }

  static void main(String[] args) throws InterruptedException {
    simple();
    //runningTask1();
    //runningTask2();
  }
}
