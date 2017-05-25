(defproject prey-hacking-clone "0.1.0-SNAPSHOT"

  :dependencies [[org.clojure/clojure "1.8.0"]
                 [helpers "1"]
                 [quil "2.6.0"]]

  :main prey-hacking-clone.main

  :target-path "target/%s"

  :profiles {:uberjar {:aot :all}})
