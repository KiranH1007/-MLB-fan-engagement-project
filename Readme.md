# MLB Fan Connect

MLB Fan Connect is an innovative engagement platform designed to revolutionize the baseball fan experience using Google Cloud's AI and data tools. It combines real-time insights, personalized content, and interactive features to connect fans with their favorite teams and players during live games.

## Features and Functionality

### Live Game Sentiment Analysis

- Uses real-time social media data (e.g., Twitter) to gauge fan sentiment during live MLB games.
- Sentiment analysis is powered by a Vertex AI model trained on historical baseball-related tweets.
- Fans receive updates about collective sentiment, like excitement during critical plays or frustration during game delays.

### Personalized Player Stats

- Displays tailored player performance stats (e.g., hits, pitching metrics) based on fan preferences.
- Fans can view individual player stats, compare performances, or access historical data.
- All data is stored and queried using Google BigQuery for quick insights.

### Interactive Fan Polls

- Offers live polls during games to engage fans with questions like “Who will score the next home run?” or “Rate this game experience.”
- Real-time poll results are updated dynamically using Google Firestore and displayed on the app.

## Technologies Used

### Google Cloud Tools

- **Vertex AI**: Model training and deployment for real-time sentiment analysis.
- **BigQuery**: Storing and querying player stats and game-related data.
- **Firestore**: Managing live poll questions and results in real-time.
- **Cloud Functions**: Hosting backend APIs for sentiment analysis and player stats queries.

### Additional Libraries

- **Python Libraries**: pandas, scikit-learn, and Tweepy for data processing and social media integration.
- **Frontend**: React.js for building a responsive and engaging user interface.

### Other Data Sources

- **MLB-provided datasets**: Player stats and game data.
- **Public datasets**: Supplementary data for training and enriching the experience.

## Findings and Learnings

- **Seamless Integration**: Google Cloud tools, particularly Vertex AI and BigQuery, provided seamless integration for real-time analytics and model deployment.
- **Optimizing Real-Time Pipelines**: Fetching and analyzing social media data live required careful pipeline optimization for speed and accuracy.
- **User Engagement Insights**: Real-time polls and personalized stats significantly enhance fan engagement, creating a more interactive game-watching experience.
