FROM openjdk:8

# Download intellij
RUN mkdir /home/intellij --parents && \
    curl https://download.jetbrains.com/idea/ideaIC-2018.1.5.tar.gz \
    --output /home/intellij/ideaDownload.tar.gz \
    --location-trusted

# Install intellij
RUN tar -xvzf /home/intellij/ideaDownload.tar.gz -C /home/intellij && \
    ipath="$(ls -la /home/intellij | grep -o 'idea-.*')" && \
    ln -s /home/intellij/$ipath /home/intellij/intellij && \
    chmod -R aog+w /home/intellij/intellij/bin

ENV IDEA_HOME=/home/intellij/intellij
