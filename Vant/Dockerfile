# Use an image with JDK 21
FROM eclipse-temurin:21-jdk-jammy

# Environment Variables
ENV ANDROID_SDK_ROOT /opt/android-sdk
ENV PATH ${PATH}:${ANDROID_SDK_ROOT}/cmdline-tools/latest/bin:${ANDROID_SDK_ROOT}/platform-tools

# Install dependencies
RUN apt-get update && apt-get install -y wget unzip git

# Download Android Command Line Tools
RUN mkdir -p ${ANDROID_SDK_ROOT}/cmdline-tools && \
  wget https://dl.google.com/android/repository/commandlinetools-linux-11076708_latest.zip -O /tmp/tools.zip && \
  unzip /tmp/tools.zip -d ${ANDROID_SDK_ROOT}/cmdline-tools && \
  mv ${ANDROID_SDK_ROOT}/cmdline-tools/cmdline-tools ${ANDROID_SDK_ROOT}/cmdline-tools/latest && \
  rm /tmp/tools.zip

# Accept Licenses and Install SDK Platforms (API 35 for 2026)
RUN yes | sdkmanager --licenses && \
  sdkmanager "platform-tools" "platforms;android-35" "build-tools;35.0.0"

WORKDIR /app
