# Sandy_E-Learning

![](https://img.shields.io/badge/Maven%20Build-Pass-brightgreen) ![](https://img.shields.io/badge/coverage-94%25-brightgreen) ![](https://img.shields.io/badge/Release%20Version-V0.3-orange) 

## Description

This project aims to establish a decentralized education platform that will offer fast and easy access to educational resources for teachers and students. Unlike typical e-learning platforms that rely on software and systems, Sandy E-learning platform uses decentralized blockchain as its core technology (The construction of the blockchain and the storage of the corresponding data were implemented using the protocols associated with Ethereum). It does not need to be integrated with existing school systems, such as linking with student information databases to achieve information integration, which is a common practice in university education websites. 

![1681897077428.png](https://static-file.hirsun.tech/2023/04/19/109216bf25dda.png)


The project provides a new solution for developing higher education by creating a secure, replicable, fast, decentralized education resource-sharing platform rather than providing a specific software. This platform enables easy access to educational resources and fosters seamless knowledge-sharing among learners and educators, promoting a more equitable and democratic approach to education.

- Demo Website: https://dapp-sandy.hanjiaming.eu.org/ (PolyU Connect ID SSO)
- Github Repository: https://github.com/guomaimang/Sandy_E-Learning
- [Blockchain Contract Logs](https://polygonscan.com/address/0xda7521f5cd54a0d2ddbaeddee280648a8c71107c)


## Basic Requirement
 

- [x] Security
    - [x] Zero Trust Model 
    - [x] Cloudflare: Prevent Brute Force Attacks / DDOS / CC
    - [x] Recaptcha V3: Prevent Crawlers and Dictionary Attack
    - [x] Password Encryption: Support SHA256 / MD5
    - [x] Prevent Replay Attacks: **Blockchain Decentralized Transaction**
    - [x] Prevent Cross-Domain: XSS、CSRF
    - [x] Global CDN: Hide the real IP of Server
    - [x] SSL: Encrypted Connection to Prevent Eavesdropping
- [x] Authentication
    - [x] SSO(Oauth2): **PolyU Connect ID Login**
    - [x] Permission Management: **PolyU ADFS Sync**
    - [x] General Login Interface
- [x] Metaverse
    - [x] Decentralization
        - [x] **Nginx** High Availability for Distributed Servers 
        - [x] Storage: User's data is placed on **Blockchain**
        - [x] Blockchain:  Implementing **Smart Contracts** from scratch with **Solidity Language**
    - [x] 2D Interface: JavaScript / Browser (Chrome V8)
    - [x] Persistent Data Storage: Blockchain
    - [x] Interaction and Sharing: Realization Education **Forum**
- [x] Education: A Platform like Blackboard 

## Enhanced Requirement

- [x] Devops/Best Practices: Kubernetes + Docker -> Complete Lifecycle of Software Engineering
- [x] Global CDN: Speed up access for users around the world
- [x] Nginx Load Balancing: ensuring business continuity
- [x] **Asynchronous** Updates Blockchain: Modern Human-Computer Interaction Experience
- [x] Modern Software Architecture: Springboot3 + JavaScript(CommonJS) + Mysql/BlockChain
    - Functional modularity and easy coupling
    - Strict and standardized Restful architecture
    - Complete front and back-end separation

![1681890308123.png](https://static-file.hirsun.tech/2023/04/19/d4fcf7e9cde39.png)

![1681890498009.png](https://static-file.hirsun.tech/2023/04/19/bdd816de1f7e8.png)
