# Build environment for Vant — Android API 35 with JDK 17 (required by AGP 9.x)
FROM eclipse-temurin:17-jdk-jammy

# ── Environment Variables ────────────────────────────────────────────────────
ENV ANDROID_SDK_ROOT=/opt/android-sdk
ENV PATH="${PATH}:${ANDROID_SDK_ROOT}/cmdline-tools/latest/bin:${ANDROID_SDK_ROOT}/platform-tools"

# ── System dependencies ──────────────────────────────────────────────────────
RUN apt-get update && apt-get install -y --no-install-recommends \
      wget \
      unzip \
      git \
    && rm -rf /var/lib/apt/lists/*

# ── Android Command Line Tools ───────────────────────────────────────────────
RUN mkdir -p "${ANDROID_SDK_ROOT}/cmdline-tools" && \
    wget -q https://dl.google.com/android/repository/commandlinetools-linux-11076708_latest.zip \
         -O /tmp/tools.zip && \
    unzip -q /tmp/tools.zip -d "${ANDROID_SDK_ROOT}/cmdline-tools" && \
    mv "${ANDROID_SDK_ROOT}/cmdline-tools/cmdline-tools" \
       "${ANDROID_SDK_ROOT}/cmdline-tools/latest" && \
    rm /tmp/tools.zip

# ── Accept Licenses and Install SDK Platforms ────────────────────────────────
RUN yes | sdkmanager --licenses && \
    sdkmanager \
      "platform-tools" \
      "platforms;android-35" \
      "build-tools;35.0.0"

WORKDIR /app

