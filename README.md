# GitHub Fork Sync Tool

## Overview

The GitHub Fork Sync Tool is a utility built with Quarkus and GraalVM that automates the process of keeping all your GitHub repository forks up to date with their respective upstream repositories. It compiles to a native image for improved performance.

## Features

- Automatically fetches updates from the upstream repository.
- Supports multiple repositories in a batch update.

## Prerequisites

- GitHub Personal Access Token with appropriate permissions

## Usage

```
services:
  repo-sync:
    image: ghcr.io/martinwitt/repo-syncer:master
    environment:
      GITHUB_TOKEN: <your github token>
      WAITTIME_CRON: 0 */6 * * *  # every 6 hours optional. This defines the interval for the sync process.
      WAITTIME_DURATION: 6H       # 6 hours 

```

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
