# MLB Fan Connect

**Author & Maintainer:** [Kiran Hiremath]

**Overview**

MLB Fan Connect is a mobile application designed to enhance the baseball fan experience. It leverages Google Cloud technologies to provide personalized content recommendations, real-time sentiment analysis (planned), and interactive features.

**Development Process**

*   **Colaboratory (Colab):** 
    *   The core recommendation logic was initially developed and tested in Google Colaboratory. 
    *   Colab provided a convenient environment for data exploration, model experimentation, and rapid prototyping. 
    *   Key steps in Colab included:
        *   Loading and preprocessing datasets from Google Cloud Storage. 
        *   Creating data structures like `user_player_mapping` and `player_content_mapping`.
        *   Implementing the recommendation algorithm based on user preferences and content popularity.
        *   Testing and refining the recommendation logic.

*   **Android Studio:**
    *   The application was built using Android Studio with Kotlin as the primary language. 
    *   Key features implemented in Android Studio include:
        *   User authentication with Firebase Authentication.
        *   User interface (UI) design using Jetpack Compose for a modern and responsive experience.
        *   Navigation between screens (Login, Signup, Main, Search, Favorites).
        *   Integration of the Cloud Function for real-time content recommendations.
        *   Implementation of user interactions like liking, commenting, and sharing.
        *   Basic profile functionality (displaying username, placeholder for favorite teams and followed players).

**Key Technologies**

*   **Google Cloud:**
    *   **Cloud Functions:** Serverless execution environment for the recommendation system.
    *   **Cloud Storage:** Stores datasets and model artifacts.
    *   **Colaboratory (Colab):** Used for initial development, testing, and data exploration.
    *   **Firebase:** Authentication, Firestore for user data storage.
*   **Android:** 
    *   **Android Studio:** Integrated Development Environment (IDE).
    *   **Kotlin:** Programming language.
    *   **Jetpack Compose:** UI toolkit.
*   **Libraries:**
    *   **Python:** For data processing and recommendation logic in Colab.
    *   **Firebase SDKs:** For authentication and database interaction.
    *   **Gson:** For JSON parsing in the Cloud Function.
    *   **Other relevant Android libraries:** For UI components, networking, etc.

**Data Sources**

*   The initial dataset for content recommendations was obtained from [https://github.com/MajorLeagueBaseball/google-cloud-mlb-hackathon](https://github.com/MajorLeagueBaseball/google-cloud-mlb-hackathon). 

**Current Features**

*   User registration and login with Firebase Authentication.
*   Basic user profile with username (placeholder for more details).
*   Content recommendation system powered by a Cloud Function.
*   Simple UI with basic navigation between screens.
*   Placeholder UI for post interactions (like, comment, share, save).
*   **Testing:**  
      * No extensive testing has been conducted yet. However, unit testing, UI testing, integration testing, and manual testing are planned for future development phases.

**Future Enhancements**

*   **Real-time Sentiment Analysis:** Integrate Vertex AI models for real-time sentiment analysis of social media data during live games.
*   **Interactive Features:** 
    *   Implement live polls and Q&A sessions using Firestore and Cloud Functions.
    *   Enable users to create and join fan groups.
*   **Personalized Player Stats:** Implement personalized player statistics dashboards using BigQuery for data storage and analysis.
*   **Enhanced Profile Features:** 
    *   Allow users to update their profile information (favorite teams, followed players).
    *   Integrate profile picture uploads with Cloud Storage.
*   **Content Creation:** Allow users to create and share their own posts.
*   **Improved UI/UX:** 
    *   Enhance the overall user experience with better animations and visual design.
    *   Implement accessibility features.

**Screenshots**

*   [Include screenshots of the Login Screen, Main Screen, and other key screens of your application here]

**Note:** This README provides a high-level overview of the project. More detailed documentation, including code comments, design documents, and testing reports, should be maintained within the project repository.

I hope this updated README effectively reflects the development of your MLB Fan Connect application!