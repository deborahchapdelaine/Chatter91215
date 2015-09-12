(ns chatter.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [hiccup.page :as page]
            [hiccup.form :as form]
            [ring.middleware.params :refer [wrap-params]]))

(defn generate-message-view
  "This generates the HTML for displaying messages"
  [messages]
  (page/html5
   [:head
    [:title "chatter"]]
   [:body
    [:h1 "Our Chat App"]
    [:p
     (form/form-to
     [:post "/"]
     "Name: " (form/text-field "name")
     "Message: " (form/text-field "msg")
     (form/submit-button "Submit"))]
    [:p
      [:table
      (map (fn [m] [:tr [:td (:name m)] [:td (:message m)]]) messages)]]]))

(def chat-messages
  (atom [{:name "blue" :message "hello, world"}
                    {:name "red" :message "red is my favorite color"}
                    {:name "green" :message "green makes it go faster"}]))

(defn update-messages!
  "This will update a message list atom"
  [messages name new-message]
  (swap! messages conj {:name name :message new-message}))

(defroutes app-routes
  (GET "/" [] (generate-message-view @chat-messages))
  (POST "/" {params :params} (generate-message-view
                               (update-messages! chat-messages
(get params "name") (get params "msg"))
                               ))
  (route/not-found "Not Found"))



;(def app
  ;(wrap-defaults app-routes site-defaults))
(def app (wrap-params app-routes))



