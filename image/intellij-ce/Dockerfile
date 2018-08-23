FROM openjdk:8

ARG INTELLIJ_VERSION

# Download intellij
RUN mkdir /home/intellij --parents && \
    curl https://download-cf.jetbrains.com/idea/ideaIC-${INTELLIJ_VERSION}.tar.gz \
    --output /home/intellij/ideaDownload.tar.gz \
    --location-trusted

# Install intellij
RUN tar -xvzf /home/intellij/ideaDownload.tar.gz -C /home/intellij && \
    ipath="$(ls -la /home/intellij | grep -o 'idea-.*')" && \
    ln -s /home/intellij/$ipath /home/intellij/intellij && \
    chmod -R aog+w /home/intellij/intellij/bin

ENV IDEA_HOME=/home/intellij/intellij
