(ns keepblog.models.article
  (:use [korma.db :refer [defdb mysql]]
        [korma.core :exclude [update]])
  (:require [clojure.string :as string] 
            [noir.util.crypt :as crypt]
            [noir.validation :as vali]))

